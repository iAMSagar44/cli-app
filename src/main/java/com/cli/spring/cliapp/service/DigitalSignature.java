package com.cli.spring.cliapp.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.FileDescriptor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

@ShellComponent
public class DigitalSignature {

    private static final Path FILE_PATH = Path.of("src/main/resources/testwallet.p12");
    private static final String PASSWORD = "password";
    private static final String ALIAS = "business";

    @ShellMethod(key = "signMessage", value = "Sign a message")
    public String signMessage(String keystore, String password, String alias, String file) throws Exception {
        Path keystorePath = retrievePath(keystore);
        PrivateKey privateKey = Utils.getPrivateKey(keystorePath, password, alias);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);

        byte[] messageBytes = Files.readAllBytes(retrievePath(file));
        signature.update(messageBytes);
        byte[] digitalSignature = signature.sign();

        Path signedFile = Files.write(Path.of(Path.of(file).getParent().toString(), "signed_message"), digitalSignature);

        return String.format("Message signed. File %s created, at location %s", signedFile.getFileName(), signedFile.getParent().toUri());

    }

    @ShellMethod(key = "verifyMessage", value = "Verify the signature of the message")
    public String verifyMessage(String keystore, String password, String alias, String file) throws Exception {
        Path keystorePath = retrievePath(keystore);
        PublicKey publicKey = Utils.getPublicKey(keystorePath, password, alias);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);

        byte[] signedMessageBytes = Files.readAllBytes(Path.of(Path.of(file).getParent().toString(), "signed_message"));

        byte[] messageBytes = Files.readAllBytes(retrievePath(file));
        signature.update(messageBytes);

        return (signature.verify(signedMessageBytes) ? "Verification Successful" : "Verification Failed");
    }

        Path retrievePath(String keystorePath){
        return Path.of(keystorePath);
    }

}
