package com.fuse;

import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;

public class Smbj {

    public void doStuff() throws Exception {
        SMBClient client = new SMBClient();

        String username = "PUTUSERNAMEHERE";
        String password = "PUTPASSWORDHERE";

        try (Connection connection = client.connect("192.168.1.35")) {
            AuthenticationContext ac = new AuthenticationContext(username, password.toCharArray(), "DOMAIN");
            Session session = connection.authenticate(ac);
    
            // Connect to Share
            try (DiskShare share = (DiskShare) session.connectShare("src")) {
                for (FileIdBothDirectoryInformation f : share.list("xbox", "*.*")) {
                    System.out.println("File : " + f.getFileName());
                }
            }
        }
    }

    public static void main(String args[]) {
        Smbj smbj = new Smbj();
        try {
        smbj.doStuff();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}