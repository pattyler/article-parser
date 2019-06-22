package io.github.patfromthe90s;

import java.net.MalformedURLException;
import java.net.URL;

import io.github.patfromthe90s.dao.SimpleSiteDao;
import io.github.patfromthe90s.dao.SiteDao;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws MalformedURLException
    {
        SiteDao siteDao = new SimpleSiteDao();
        siteDao.getLastUpdated(new URL("test"));
    }
}
