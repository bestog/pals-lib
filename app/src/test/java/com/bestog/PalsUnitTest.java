package com.bestog;

import android.content.Context;

import com.bestog.pals.Pals;
import com.bestog.pals.provider.LocationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PalsUnitTest {

    @Mock
    Context mMockContext;

    @Test
    public void testEnabledAndDisabledProviders() {
        Pals pals = new Pals(mMockContext);
        String result = pals.getEnabledProviderList().toString();
        assertThat(result, is("[]"));
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, null);
        pals.enableProvider(LocationProvider.PROVIDER_OPENCELLID, null);
        assertThat(pals.getEnabledProviderList().toString(), is("[OpenCellIDLocation, MozillaLocation]"));
        pals.disableProvider(LocationProvider.PROVIDER_MOZILLA);
        assertThat(pals.getEnabledProviderList().toString(), is("[OpenCellIDLocation]"));
    }

    @Test
    public void testIsProviderEnabled() {
        Pals pals = new Pals(mMockContext);
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, null);
        assertTrue(pals.isProviderEnabled(LocationProvider.PROVIDER_MOZILLA));
    }

    @Test
    public void testOwnTokenForLocationProvider() {
        Pals pals = new Pals(mMockContext);
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, "01234567890123456789");
        LocationProvider lp = pals.getEnabledProviders().get(LocationProvider.PROVIDER_MOZILLA);
        assertThat(lp.getOwnToken(), is("01234567890123456789"));
    }

    @Test
    public void testPals() {
        Pals pals = new Pals(mMockContext);
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, null);
    }
}