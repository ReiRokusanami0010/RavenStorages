package raven.ravenstorages.common.registries.handler;

import net.minecraftforge.eventbus.api.IEventBus;
import raven.ravenstorages.common.registries.RavenBlock;
import raven.ravenstorages.common.registries.RavenItem;

@Deprecated
public class RegistrationHandler {
    public static void onRegistration(IEventBus eventBus) {
        RavenItem.onRegistration(eventBus);
        RavenBlock.onRegistration(eventBus);
    }
}
