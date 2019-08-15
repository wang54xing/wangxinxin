package com.fulaan.utils;

import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wang_xinxin on 2015/6/4.
 */
public class DigestUtils {
    private static final int STREAM_BUFFER_LENGTH = 1024;

    public DigestUtils() {
    }

    private static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
        byte[] buffer = new byte[1024];

        for(int read = data.read(buffer, 0, 1024); read > -1; read = data.read(buffer, 0, 1024)) {
            digest.update(buffer, 0, read);
        }

        return digest.digest();
    }

    private static byte[] getBytesUtf8(String data) {
        return StringUtils.getBytesUtf8(data);
    }

    static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException(var2.getMessage());
        }
    }

    private static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }

    private static MessageDigest getSha256Digest() {
        return getDigest("SHA-256");
    }

    private static MessageDigest getSha384Digest() {
        return getDigest("SHA-384");
    }

    private static MessageDigest getSha512Digest() {
        return getDigest("SHA-512");
    }

    private static MessageDigest getShaDigest() {
        return getDigest("SHA");
    }

    public static byte[] md5(byte[] data) {
        return getMd5Digest().digest(data);
    }

    public static byte[] md5(InputStream data) throws IOException {
        return digest(getMd5Digest(), data);
    }

    public static byte[] md5(String data) {
        return md5((byte[])getBytesUtf8(data));
    }

    public static String md5Hex(byte[] data) {
        return Hex.encodeHexString(md5((byte[]) data));
    }

    public static String md5Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(md5((InputStream)data));
    }

    public static String md5Hex(String data) {
        return Hex.encodeHexString(md5((String)data));
    }

    public static byte[] sha(byte[] data) {
        return getShaDigest().digest(data);
    }

    public static byte[] sha(InputStream data) throws IOException {
        return digest(getShaDigest(), data);
    }

    public static byte[] sha(String data) {
        return sha((byte[])getBytesUtf8(data));
    }

    public static byte[] sha256(byte[] data) {
        return getSha256Digest().digest(data);
    }

    public static byte[] sha256(InputStream data) throws IOException {
        return digest(getSha256Digest(), data);
    }

    public static byte[] sha256(String data) {
        return sha256((byte[])getBytesUtf8(data));
    }

    public static String sha256Hex(byte[] data) {
        return Hex.encodeHexString(sha256((byte[])data));
    }

    public static String sha256Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha256((InputStream)data));
    }

    public static String sha256Hex(String data) {
        return Hex.encodeHexString(sha256((String)data));
    }

    public static byte[] sha384(byte[] data) {
        return getSha384Digest().digest(data);
    }

    public static byte[] sha384(InputStream data) throws IOException {
        return digest(getSha384Digest(), data);
    }

    public static byte[] sha384(String data) {
        return sha384((byte[])getBytesUtf8(data));
    }

    public static String sha384Hex(byte[] data) {
        return Hex.encodeHexString(sha384((byte[])data));
    }

    public static String sha384Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha384((InputStream)data));
    }

    public static String sha384Hex(String data) {
        return Hex.encodeHexString(sha384((String)data));
    }

    public static byte[] sha512(byte[] data) {
        return getSha512Digest().digest(data);
    }

    public static byte[] sha512(InputStream data) throws IOException {
        return digest(getSha512Digest(), data);
    }

    public static byte[] sha512(String data) {
        return sha512((byte[])getBytesUtf8(data));
    }

    public static String sha512Hex(byte[] data) {
        return Hex.encodeHexString(sha512((byte[])data));
    }

    public static String sha512Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha512((InputStream)data));
    }

    public static String sha512Hex(String data) {
        return Hex.encodeHexString(sha512((String)data));
    }

    public static String shaHex(byte[] data) {
        return Hex.encodeHexString(sha((byte[])data));
    }

    public static String shaHex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha((InputStream)data));
    }

    public static String shaHex(String data) {
        return Hex.encodeHexString(sha((String)data));
    }
}
