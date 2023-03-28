private Character encodeCharacter(Character c) {
        if (c == ' ') {
            return ' ';
        }
        Integer charPosition = ALPHABET.indexOf(c);
        Integer encryptedCharPosition = (charPosition + 13) % 26;
        Character encryptedChar = ALPHABET.get(encryptedCharPosition);
        return encryptedChar;
    }
--------------------

public static String decrypt( String encryptedText ) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance( ALGORITHM );
		c.init( Cipher.DECRYPT_MODE, key );
		byte[] decordedValue = new BASE64Decoder().decodeBuffer( encryptedText );
		byte[] decValue = c.doFinal( decordedValue );
		String decryptedValue = new String( decValue );
		return decryptedValue;
	}
--------------------

public void encrypt() {
        try {
            // Controleer input
            if(pfPassword.getCharacters().length() <= 0) {
                throw new IllegalArgumentException("Voer een wachtwoord in");
            }
            if(taMessage.getText() == null || taMessage.getText().isEmpty()) {
                throw new IllegalArgumentException("Voer een bericht in");
            }
            File file = fileChooser.showSaveDialog(stage);
            if(file == null) {
                throw new IllegalArgumentException("Kies een file");
            }
            
            // Genereer salt
            byte[] salt = new byte[saltLength];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            byte[] encryptedMessage = crypt(Cipher.ENCRYPT_MODE, taMessage.getText().getBytes(), salt);

            // Wegschrijven naar file
            FileOutputStream out = new FileOutputStream(file);
            DataOutputStream out2 = new DataOutputStream(out);
            for(byte b : salt) {
                out2.writeByte(b);
            }
            for(byte b : encryptedMessage) {
                out2.writeByte(b);
            }
            
            taMessage.clear();
            JOptionPane.showMessageDialog(null, "Bericht is succesvol weggeschreven");
        } catch (IllegalArgumentException iaEx) {
            JOptionPane.showMessageDialog(null, iaEx.getMessage());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
--------------------

