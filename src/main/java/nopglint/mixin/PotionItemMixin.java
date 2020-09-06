package nopglint.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import nopglint.PGlintInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {

    public PotionItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author .
     */
    @Overwrite
    public boolean hasGlint(ItemStack stack) {
        if(PGlintInitializer.getEnabled()){
            return false;
        }
        return super.hasGlint(stack) || !PotionUtil.getPotionEffects(stack).isEmpty();
    }
}
