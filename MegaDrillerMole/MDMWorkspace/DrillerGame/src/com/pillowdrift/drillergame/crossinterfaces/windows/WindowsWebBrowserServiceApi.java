package com.pillowdrift.drillergame.crossinterfaces.windows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.pillowdrift.drillergame.crossinterfaces.WebBrowserServiceApi;

/**
 * Implementation of web browser services for windows
 * @author cake_cruncher_7
 *
 */
public class WindowsWebBrowserServiceApi implements WebBrowserServiceApi
{
	//FUNCTION
	@Override
	public void openUrl(String url)
	{
		//Try to launch a web browser as requested
		URI uri;
		try {
			uri = new URI(url);
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
