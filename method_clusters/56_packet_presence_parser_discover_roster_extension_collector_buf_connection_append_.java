public static boolean verifyDiscoverInfoVersion(String ver, String hash, DiscoverInfo info) {
        // step 3.3 check for duplicate identities
        if (info.containsDuplicateIdentities())
            return false;

        // step 3.4 check for duplicate features
        if (info.containsDuplicateFeatures())
            return false;

        // step 3.5 check for well-formed packet extensions
        if (verifyPacketExtensions(info))
            return false;

        String calculatedVer = generateVerificationString(info, hash);

        if (!ver.equals(calculatedVer))
            return false;

        return true;
    }
--------------------

private void changeAffiliationByAdmin(String jid, String affiliation, String reason)
            throws XMPPException {
        MUCAdmin iq = new MUCAdmin();
        iq.setTo(room);
        iq.setType(IQ.Type.SET);
        // Set the new affiliation.
        MUCAdmin.Item item = new MUCAdmin.Item(affiliation, null);
        item.setJid(jid);
        if(reason != null)
            item.setReason(reason);
        iq.addItem(item);

        // Wait for a response packet back from the server.
        PacketFilter responseFilter = new PacketIDFilter(iq.getPacketID());
        PacketCollector response = connection.createPacketCollector(responseFilter);
        // Send the change request to the server.
        connection.sendPacket(iq);
        // Wait up to a certain number of seconds for a reply.
        IQ answer = (IQ) response.nextResult(SmackConfiguration.getPacketReplyTimeout());
        // Stop queuing results
        response.cancel();

        if (answer == null) {
            throw new XMPPException("No response from server.");
        }
        else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }
--------------------

public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        boolean done = false;
        MultipleAddresses multipleAddresses = new MultipleAddresses();
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("address")) {
                    String type = parser.getAttributeValue("", "type");
                    String jid = parser.getAttributeValue("", "jid");
                    String node = parser.getAttributeValue("", "node");
                    String desc = parser.getAttributeValue("", "desc");
                    boolean delivered = "true".equals(parser.getAttributeValue("", "delivered"));
                    String uri = parser.getAttributeValue("", "uri");
                    // Add the parsed address
                    multipleAddresses.addAddress(type, jid, node, desc, delivered, uri);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(multipleAddresses.getElementName())) {
                    done = true;
                }
            }
        }
        return multipleAddresses;
    }
--------------------

