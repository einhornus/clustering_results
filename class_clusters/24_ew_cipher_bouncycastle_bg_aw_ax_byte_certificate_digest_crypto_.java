package org.ripple.bouncycastle.crypto.params;

import org.ripple.bouncycastle.crypto.CipherParameters;

public class RC2Parameters
    implements CipherParameters
{
    private byte[]  key;
    private int     bits;

    public RC2Parameters(
        byte[]  key)
    {
        this(key, (key.length > 128) ? 1024 : (key.length * 8));
    }

    public RC2Parameters(
        byte[]  key,
        int     bits)
    {
        this.key = new byte[key.length];
        this.bits = bits;

        System.arraycopy(key, 0, this.key, 0, key.length);
    }

    public byte[] getKey()
    {
        return key;
    }

    public int getEffectiveKeyBits()
    {
        return bits;
    }
}

--------------------

package backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class GenerateKey {

	int bitLength = 1024;
	SecureRandom rnd = new SecureRandom();
	BigInteger p = BigInteger.probablePrime(bitLength, rnd);
	BigInteger g = BigInteger.probablePrime(bitLength, rnd);
	
	public static String pubKeyName =  "PUBKEY";
	public static String privKeyName =  "PRIVKEY";
	
	public static String serverpubKeyName =  "ServerPUBKEY";
	public static String serverprivKeyName =  "ServerPRIVKEY";
	
	public static String clientPath = "./Client/";
	public static String consultantPath = "./Consultant/";
	public static String serverPath = "./Server/";
	public static String clientOnServerPath = "./Server/ClientPubKey/";
	public static String consOnServerPath = "./Server/ConsultantPubKey/";
	
	private KeyPair GenKeyPairSpec() {
		KeyPair kp = null;
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("DiffieHellman");
			DHParameterSpec param = new DHParameterSpec(p, g);
			kpg.initialize(param);
			kp = kpg.generateKeyPair();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kp;
	}

	private KeyPair GenKeyPairParam(PublicKey serverPubKey) {
		KeyPair kp = null;
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("DiffieHellman");
			DHParameterSpec serverDHSpec = ((DHPublicKey) serverPubKey)
					.getParams();

			DHParameterSpec dhSpec = new DHParameterSpec(serverDHSpec.getP(),
					serverDHSpec.getG(), serverDHSpec.getL());

			kpg.initialize(dhSpec);
			kp = kpg.generateKeyPair();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kp;
	}

	private byte[] GetPubKey(KeyPair kp) {
		PublicKey pubKey = kp.getPublic();
		return pubKey.getEncoded();
	}

	private byte[] GetPrivKey(KeyPair kp) {
		PrivateKey privKey = kp.getPrivate();
		return privKey.getEncoded();
	}

	private static void writeKey(byte[] key, String filename) throws IOException {
		FileOutputStream file = new FileOutputStream(filename);
		file.write(key);
		file.close();
	}

	public PublicKey ByteToPublicKey(byte[] bytes) {
		X509EncodedKeySpec pubspec = new X509EncodedKeySpec(bytes);
		KeyFactory factory;
		PublicKey pubkey = null;
		try {
			factory = KeyFactory.getInstance("DiffieHellman");
			pubkey = factory.generatePublic(pubspec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pubkey;
	}

	public static byte[] readKeyFile(String filename) {
		FileInputStream file;
		byte[] bytes = null;
		try {
			file = new FileInputStream(filename);
			bytes = new byte[file.available()];
			file.read(bytes);
			file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bytes;
	}
	
	public PrivateKey ByteToPrivate(byte[] bytes){
		PKCS8EncodedKeySpec privspec = new PKCS8EncodedKeySpec(bytes);
		KeyFactory factory;
		PrivateKey privKey = null;
		
		try {
			factory = KeyFactory.getInstance("DiffieHellman");
			privKey = factory.generatePrivate(privspec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return privKey;
	}
	
	public void createKeyUser(PublicKey serverPubKey, String clientName, int accessID){
		KeyPair kp = GenKeyPairParam(serverPubKey);
		byte[] pubKey = GetPubKey(kp);
		byte[] privKey = GetPrivKey(kp);
				
		try {
			if(accessID == 1){ //consultant
				writeKey(pubKey, consultantPath+clientName+pubKeyName);
				writeKey(privKey, consultantPath+clientName+privKeyName);
				//copy the pubkey in the server
				writeKey(pubKey, consOnServerPath+clientName+pubKeyName);
			}else if(accessID == 2){ //client
				writeKey(pubKey, clientPath+clientName+pubKeyName);
				writeKey(privKey, clientPath+clientName+privKeyName);
				//copy the pubkey in the server
				writeKey(pubKey, clientOnServerPath+clientName+pubKeyName);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void createKeyServer(){
		KeyPair kp = GenKeyPairSpec();
		byte[] pubKey = GetPubKey(kp);
		byte[] privKey = GetPrivKey(kp);
		try {
			writeKey(pubKey, serverpubKeyName);
			writeKey(privKey, serverprivKeyName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Generate Secret Key
	public SecretKey DHSecretKeyAgreeement(PublicKey otherPubkey, PrivateKey ownPrivKey){
		KeyAgreement ka;
		SecretKey secret = null;
		try {
			ka = KeyAgreement.getInstance("DiffieHellman");
			ka.init(ownPrivKey);
			
			Key key = ka.doPhase(otherPubkey, true);
			byte[] tempSec = ka.generateSecret();
			//generate 16 byte key length for AES encryption
			byte[] temp = new byte[16];
			temp = Arrays.copyOf(tempSec, 16);
			secret = new SecretKeySpec(temp, "AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return secret;
	}
	
	
	
	public static void main(String[] arg) {

	}

}

--------------------

package org.radarlab.crypto.ecdsa;

import org.ripple.bouncycastle.asn1.sec.SECNamedCurves;
import org.ripple.bouncycastle.asn1.x9.X9ECParameters;
import org.ripple.bouncycastle.crypto.params.ECDomainParameters;
import org.ripple.bouncycastle.math.ec.ECCurve;
import org.ripple.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SECP256K1 {
    private static final ECDomainParameters ecParams;
    private static final X9ECParameters params;

    static {

        params = SECNamedCurves.getByName("secp256k1");
        ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
    }

    public static ECDomainParameters params() {
        return ecParams;
    }

    public static BigInteger order() {
        return ecParams.getN();
    }


    public static ECCurve curve() {
        return ecParams.getCurve();
    }

    public static ECPoint basePoint() {
        return ecParams.getG();
    }

    static byte[] basePointMultipliedBy(BigInteger secret) {
        return basePoint().multiply(secret).getEncoded(true);
    }
}

--------------------

