package org.agmas.noellesroles.client.mixin.seer;

import dev.doctor4t.trainmurdermystery.util.AnnounceWelcomePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.agmas.noellesroles.client.NoellesrolesClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnnounceWelcomePayload.Receiver.class)
public abstract class SeerResetMixin {

    @Inject(method = "receive(Ldev/doctor4t/trainmurdermystery/util/AnnounceWelcomePayload;Lnet/fabricmc/fabric/api/client/networking/v1/ClientPlayNetworking$Context;)V", at = @At("HEAD"))
    private void b(AnnounceWelcomePayload payload, ClientPlayNetworking.Context context, CallbackInfo ci) {
       NoellesrolesClient.seer_revealedPlayers.clear();
    }
}
