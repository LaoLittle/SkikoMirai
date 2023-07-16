use std::fs::File;
use std::ptr::slice_from_raw_parts;

use imgref::{Img, ImgVec};
use jni::JNIEnv;
use jni::objects::{JByteArray, JClass, JString, ReleaseMode};
use jni::sys::{jboolean, jbyte, jbyteArray, jdouble, jint, jlong, jlongArray, JNI_TRUE, jshort};
use rgb::RGBA8;

use crate::{Collector, new, NoProgress, Repeat, Settings, Writer};

#[no_mangle]
pub extern "system" fn Java_org_laolittle_plugin_gif_GifEncoderKt_nNewEncoder(
    env: JNIEnv,
    _class: JClass,
    width: jint,
    height: jint,
    quality: jbyte,
    fast: jboolean,
    repeat: jshort,
) -> jlongArray {
    let setting = Settings {
        width: if width < 0 { None } else { Some(width as u32) },
        height: if height < 0 { None } else { Some(height as u32) },
        quality: quality as u8,
        fast: fast == JNI_TRUE,
        repeat: if repeat < 0 { Repeat::Infinite } else { Repeat::Finite(repeat as u16) },
    };

    let (c, w) = new(setting).unwrap();

    let c_raw = Box::into_raw(c.into());
    let w_raw = Box::into_raw(w.into());

    let ptr = env.new_long_array(2).unwrap();

    env.set_long_array_region(&ptr, 0, &[raw_ptr_to_long(c_raw), raw_ptr_to_long(w_raw)]).unwrap();

    ptr.into_raw()
}

#[no_mangle]
pub extern "system" fn Java_org_laolittle_plugin_gif_CollectorKt_nCollectorAddFrameBytes(
    mut env: JNIEnv,
    _class: JClass,
    bytes: JByteArray,
    frame_index: jint,
    presentation: jdouble,
    collector: jlong,
) -> jlong {
    let collector = unsafe { Box::<Collector>::from_raw(long_to_raw_ptr(collector)) };

    let len = env.get_array_length(&bytes).unwrap();
    let primitive = unsafe {
        env.get_array_elements_critical(&bytes, ReleaseMode::CopyBack).unwrap()
    };
    let data = unsafe { &*slice_from_raw_parts(primitive.as_ptr() as *const u8, len as usize) };

    if let Ok(bitmap) = lodepng::decode32(&data) {
        let image: ImgVec<RGBA8> = Img::new(bitmap.buffer.into(), bitmap.width, bitmap.height);
        collector.add_frame_rgba(frame_index as usize, image, presentation).unwrap();
    }

    let raw = Box::into_raw(collector);
    raw_ptr_to_long(raw)
}

#[no_mangle]
pub extern "system" fn Java_org_laolittle_plugin_gif_CollectorKt_nCollectorAddPngFile(
    mut env: JNIEnv,
    _class: JClass,
    path: JString,
    frame_index: jint,
    presentation: jdouble,
    collector: jlong,
) -> jlong {
    let collector = unsafe { Box::<Collector>::from_raw(long_to_raw_ptr(collector)) };
    let str = env.get_string(&path).unwrap();
    let path = str.to_str().unwrap();

    if let Ok(bitmap) = lodepng::decode32_file(path) {
        let image: ImgVec<RGBA8> = ImgVec::new(bitmap.buffer.into(), bitmap.width, bitmap.height);
        collector.add_frame_rgba(frame_index as usize, image, presentation).unwrap();
    }

    let raw = Box::into_raw(collector);
    raw_ptr_to_long(raw)
}

#[no_mangle]
pub extern "system" fn Java_org_laolittle_plugin_gif_CollectorKt_nCloseCollector(
    _env: JNIEnv,
    _class: JClass,
    collector: jlong,
) {
    unsafe { Box::<Collector>::from_raw(long_to_raw_ptr(collector)) };
}

#[no_mangle]
pub extern "system" fn Java_org_laolittle_plugin_gif_WriterKt_nWriteToFile(
    mut env: JNIEnv,
    _class: JClass,
    path: JString,
    writer: jlong,
) {
    let writer = unsafe { Box::<Writer>::from_raw(long_to_raw_ptr(writer)) };
    let str = env.get_string(&path).unwrap();
    let path = str.to_str().unwrap();
    let f = File::create(path).unwrap();

    let mut nop = NoProgress {};
    writer.write(f, &mut nop).unwrap();
}

#[no_mangle]
pub extern "system" fn Java_org_laolittle_plugin_gif_WriterKt_nWriteToBytes(
    env: JNIEnv,
    _class: JClass,
    writer: jlong,
) -> jbyteArray {
    let writer = unsafe { Box::<Writer>::from_raw(long_to_raw_ptr(writer)) };
    let mut buff: Vec<u8> = Vec::with_capacity(4096);

    let mut nop = NoProgress {};
    writer.write(&mut buff, &mut nop).unwrap();

    **env.byte_array_from_slice(&buff).unwrap()
}

fn raw_ptr_to_long<T>(ptr: *const T) -> i64 {
    ptr as usize as i64
}

fn long_to_raw_ptr<T>(value: i64) -> *mut T {
    value as usize as *mut T
}