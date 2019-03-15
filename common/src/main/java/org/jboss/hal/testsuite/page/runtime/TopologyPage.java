package org.jboss.hal.testsuite.page.runtime;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.TopologyPreviewFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;

import static org.jboss.hal.resources.Ids.DOMAIN_BROWSE_BY;

@Place(NameTokens.RUNTIME)
public class TopologyPage extends BasePage {

    @Override
    public void navigate() {
        navigateAndReturnTopologyFragment();
    }

    public TopologyPreviewFragment navigateToTopologyFragment() {
        return navigateAndReturnTopologyFragment();
    }

    private TopologyPreviewFragment navigateAndReturnTopologyFragment() {
        return console.finder(NameTokens.RUNTIME, new FinderPath().append(DOMAIN_BROWSE_BY, "topology"))
                .preview(TopologyPreviewFragment.class);
    }
}
