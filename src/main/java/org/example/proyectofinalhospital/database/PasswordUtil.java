package org.example.proyectofinalhospital.database;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Hash de contraseñas PBKDF2 con sal aleatoria por cuenta. */
public final class PasswordUtil {

    private static final int ITERACIONES = 210_000;
    private static final int BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {}

    public static String hash(String contrasena) {

        byte[] sal = new byte[16];
        RANDOM.nextBytes(sal);
        byte[] resultado = derivar(contrasena.toCharArray(), sal, ITERACIONES);

        return ITERACIONES + "$" + Base64.getEncoder().encodeToString(sal) + "$"
                + Base64.getEncoder().encodeToString(resultado);
    }

    public static boolean verificar(String contrasena, String almacenado) {

        try {
            String[] partes = almacenado.split("\\$", -1);
            if (partes.length != 3) return false;
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] sal = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            return MessageDigest.isEqual(esperado, derivar(
                    contrasena.toCharArray(),
                    sal,
                    iteraciones)
            );
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private static byte[] derivar(char[] contrasena, byte[] sal, int iteraciones) {

        PBEKeySpec spec = new PBEKeySpec(contrasena, sal, iteraciones, BITS);

        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo proteger la contraseña", ex);
        } finally {
            spec.clearPassword();
            java.util.Arrays.fill(contrasena, '\0');
        }
    }
}