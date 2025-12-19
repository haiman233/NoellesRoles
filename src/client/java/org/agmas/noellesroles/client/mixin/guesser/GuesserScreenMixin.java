package org.agmas.noellesroles.client.mixin.guesser;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedHandledScreen;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.agmas.noellesroles.Noellesroles;
import org.agmas.noellesroles.client.ui.guesser.GuesserPlayerWidget;
import org.agmas.noellesroles.client.ui.guesser.GuesserRoleWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(LimitedInventoryScreen.class)
public abstract class GuesserScreenMixin extends LimitedHandledScreen<PlayerScreenHandler>{
    @Shadow @Final public ClientPlayerEntity player;

    public GuesserScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }


    @Inject(method = "init", at = @At("TAIL"))
    void renderGuesserHeads(CallbackInfo ci) {
        GameWorldComponent gameWorldComponent = (GameWorldComponent) GameWorldComponent.KEY.get(player.getWorld());
        GuesserPlayerWidget.selectedPlayer = null;
        if (gameWorldComponent.isRole(player,Noellesroles.GUESSER)) {
            GuesserRoleWidget.stopClosing = false;
            List<AbstractClientPlayerEntity> entries = MinecraftClient.getInstance().world.getPlayers();
            GameWorldComponent worldComponent = GameWorldComponent.KEY.get(player.getWorld());
            entries.removeIf((e) -> {
                return !worldComponent.isInnocent(e);
            });
            int apart = 36;
            int x = ((LimitedInventoryScreen)(Object)this).width / 2 - (entries.size()) * apart / 2 + 9;
            int shouldBeY = (((LimitedInventoryScreen)(Object)this).height - 32) / 2;
            int y = shouldBeY + 80;

            for(int i = 0; i < entries.size(); ++i) {
                GuesserPlayerWidget child = new GuesserPlayerWidget(((LimitedInventoryScreen)(Object)this), x + apart * i, y, entries.get(i).getUuid(), player.networkHandler.getPlayerListEntry(entries.get(i).getUuid()));
                addDrawableChild(child);
                child.visible = false;
            }

            GuesserRoleWidget child = new GuesserRoleWidget(((LimitedInventoryScreen)(Object)this), textRenderer, (width/2)-100, y);
            addDrawableChild(child);
            child.setVisible(false);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    void swapGuesserModes(CallbackInfo ci) {
        for (Element child : children()) {
            GuesserRoleWidget.stopClosing = GuesserPlayerWidget.selectedPlayer != null;
            if (child instanceof GuesserPlayerWidget gpw) {
                gpw.visible = GuesserPlayerWidget.selectedPlayer == null;
            }
            if (child instanceof GuesserRoleWidget grw) {
                grw.visible = GuesserPlayerWidget.selectedPlayer != null;
            }
        }
    }

}
