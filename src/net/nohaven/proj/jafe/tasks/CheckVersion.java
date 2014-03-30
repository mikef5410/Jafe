package net.nohaven.proj.jafe.tasks;

import java.io.*;
import java.net.*;

import net.nohaven.proj.jafe.utils.*;

/**
 * Checks the version. It fetches the page at 
 * 
 * http://www.nohaven.net/projects/java/jafe/latest.php
 * 
 * that has written in it the version between ">>" and "<<", and the build
 * number between "))" and "((". Then confronts them with the ones of the
 * current app, in Constants.java 
 */
public class CheckVersion extends Thread {
	private Action actionIfOk;

	private Action actionIfKo;

	private Action actionIfError;

	private boolean verbose;

	public CheckVersion(Action ifOk, Action ifKo, Action ifError,
			boolean verbose) {
		super();
		actionIfOk = ifOk;
		actionIfKo = ifKo;
		actionIfError = ifError;
		this.verbose = verbose;
	}

	public void run() {
		try {
			URL url = new URL(
					"http://www.nohaven.net/projects/java/jafe/latest.php");

			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setConnectTimeout(10000); //10 sec

			if (huc.getResponseCode() != HttpURLConnection.HTTP_OK)
				throw new IOException();

			BufferedReader bufr = new BufferedReader(new InputStreamReader(huc
					.getInputStream()));

			String version = null;
			int build = Integer.MAX_VALUE;

			String row = null;
			while ((row = bufr.readLine()) != null) {
				int ini = row.indexOf(">>");
				int fin = row.indexOf("<<");
				if (ini > -1 && fin > ini)
					version = row.trim().substring(ini + 2, fin - ini);

				ini = row.indexOf("))");
				fin = row.indexOf("((");
				if (ini > -1 && fin > ini)
					build = Integer.parseInt(row.trim().substring(ini + 2,
							fin - ini));
				
				if (build < Integer.MAX_VALUE && version != null)
					break;
			}

			if (Constants.BUILD >= build) {
				if (verbose)
					actionIfOk.execute();
			} else {
				actionIfKo.execute(version);
			}
		} catch (IOException ioe) {
			if (verbose)
				actionIfError.execute();
		}
	}
}
