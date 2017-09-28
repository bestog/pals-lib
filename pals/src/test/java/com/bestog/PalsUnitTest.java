package com.bestog;

import android.content.Context;

import com.bestog.pals.Pals;
import com.bestog.pals.provider.LocationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PalsUnitTest {

    @Mock
    Context mMockContext;

    @Test
    public void testEnabledAndDisabledProviders() {
        Pals pals = new Pals(mMockContext);
        assertThat(pals.getEnabledProviderList().length, is(0));
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA);
        pals.enableProvider(LocationProvider.PROVIDER_OPENCELLID, "123456789");
        assertThat(pals.getEnabledProviderList().length, is(2));
        pals.disableProvider(LocationProvider.PROVIDER_MOZILLA);
        assertThat(pals.getEnabledProviderList().length, is(1));
    }

    @Test
    public void testIsDefaultProviderEnabled() {
        Pals pals = new Pals(mMockContext);
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA);
        assertTrue(pals.isProviderEnabled(LocationProvider.PROVIDER_MOZILLA));
    }

    @Test
    public void testIsProviderEnabled() {
        Pals pals = new Pals(mMockContext);
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, "123456789");
        assertTrue(pals.isProviderEnabled(LocationProvider.PROVIDER_MOZILLA));
    }

    @Test
    public void testNameFromLocationProvider() {
        Pals pals = new Pals(mMockContext);
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, "01234567890123456789");
        LocationProvider lp = pals.getEnabledProviders().get(LocationProvider.PROVIDER_MOZILLA);
        assertThat(lp.getTitle(), is(LocationProvider.PROVIDER_MOZILLA));
    }

    @Test
    public void testPals() {
        Pals pals = new Pals(mMockContext);
        pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, null);
    }
}