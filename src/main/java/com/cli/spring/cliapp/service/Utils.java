package com.cli.spring.cliapp.service;

import java.nio.file.Path;
import java.security.*;


public class Utils {

    static PrivateKey getPrivateKey(Path path, String password, String alias) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(path.toFile(), password.toCharArray());
        return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
    }

    static PublicKey getPublicKey(Path path, String password, String alias) throws Exception{
        KeyStore keystore = KeyStore.getInstance(path.toFile(), password.toCharArray());
        return keystore.getCertificate(alias).getPublicKey();
    }

}
