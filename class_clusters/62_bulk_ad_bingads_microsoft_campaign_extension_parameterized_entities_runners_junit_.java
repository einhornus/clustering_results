package com.microsoft.bingads.v13.api.test.entities.campaign.write;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.microsoft.bingads.internal.functionalinterfaces.BiConsumer;
import com.microsoft.bingads.v13.api.test.entities.campaign.BulkCampaignTest;
import com.microsoft.bingads.v13.bulk.entities.BulkCampaign;

@RunWith(Parameterized.class)
public class BulkCampaignWriteAdScheduleUseSearcherTimeZone extends BulkCampaignTest {

    @Parameterized.Parameter(value = 1)
    public Boolean propertyValue;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"true", true},
                {"false", false},
                {null, null}
        });
    }

    @Test
    public void testWrite() {
        testWriteProperty("Ad Schedule Use Searcher Time Zone", this.datum, this.propertyValue, new BiConsumer<BulkCampaign, Boolean>() {
            @Override
            public void accept(BulkCampaign c, Boolean v) {
                c.getCampaign().setAdScheduleUseSearcherTimeZone(v);
            }
        });
    }
}

--------------------

package com.microsoft.bingads.v13.api.test.entities.ad_extension.video;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.microsoft.bingads.v13.api.test.entities.ad_extension.video.read.BulkVideoAdExtensionReadTests;
import com.microsoft.bingads.v13.api.test.entities.ad_extension.video.write.BulkVideoAdExtensionWriteTests;

@RunWith(Suite.class)
@SuiteClasses({
        BulkVideoAdExtensionReadTests.class,
        BulkVideoAdExtensionWriteTests.class
})
public class BulkVideoAdExtensionTests {

}

--------------------

package net.glowstone.constants;

import net.glowstone.testutils.ParameterUtils;
import org.bukkit.block.Biome;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Tests for {@link GlowBiome}.
 */
@RunWith(Parameterized.class)
public class BiomeTest {

    private final Biome biome;

    public BiomeTest(Biome biome) {
        this.biome = biome;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return ParameterUtils.enumCases(Biome.values());
    }

    @Test
    public void testIdMapping() {
        int id = GlowBiome.getId(biome);
        assertFalse("No id specified for biome " + biome, id == -1);
        assertEquals("Mapping for id " + id + " mismatch", biome, GlowBiome.getBiome(id));
    }

}

--------------------

