package invoker54.invocore.client;

import invoker54.invocore.Invocore;
import net.minecraft.client.audio.BeeSound;
import net.minecraft.client.audio.LocatableSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Invocore.MOD_ID)
public class InvoSound extends LocatableSound {
    private static final Logger LOGGER = LogManager.getLogger();
    private long startTick = -1;
    private int invoDelay = 0;
    private boolean delayedStart = false;

    private IModifySound preModifySound;

    private final SoundEvent mySoundEvent;
    private final SoundCategory mySoundCategory;

    private static SoundHandler soundHandler;
    private World savedWorld = null;


    public InvoSound(SoundEvent soundIn, SoundCategory categoryIn) {
        super(soundIn, categoryIn);
        this.mySoundEvent = soundIn;
        this.mySoundCategory = categoryIn;
    }

    public static SoundHandler getSoundHandler(){
        if (soundHandler == null) soundHandler = ClientUtil.mC.getSoundHandler();
        return soundHandler;
    }

    public boolean isDonePlaying() {
//        return this.mySource == null || this.mySource.isStopped();
        return getSoundHandler().isPlaying(this) == false;
    }

    public InvoSound duplicate(){
        return new InvoSound(this.mySoundEvent, this.mySoundCategory).setPreModifySound(this.preModifySound)
                .setVolume(this.volume).setPitch(this.pitch).setPos(new Vector3d(this.x, this.y, this.z))
                .setRepeatDelay(this.invoDelay, this.delayedStart).setAttenuation(this.attenuationType)
                .setGlobal(this.global);
    }

    public InvoSound setPreModifySound(IModifySound newModifier){
        this.preModifySound = newModifier;
        return this;
    }

    public InvoSound setVolume(float volume){
        this.volume = volume;
        return this;
    }

    public InvoSound setPitch(float pitch){
        this.pitch = pitch;
        return this;
    }

    public InvoSound setPos(Vector3d pos){
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        return this;
    }

    public InvoSound setRepeatDelay(int delayInTicks, boolean delayedStart){
        this.invoDelay = delayInTicks;
        this.delayedStart = delayedStart;
        return this;
    }

    public InvoSound setAttenuation(AttenuationType type){
        this.attenuationType = type;
        return this;
    }

    public InvoSound setGlobal(boolean global){
        this.global = global;
        return this;
    }

    public void playWhenStopped(){
        if (this.isDonePlaying()) this.play();
    }

    public void play() {
        if (this.startTick == 0) this.startTick = this.currentTime();

        if (this.startTick > -1 && this.currentTime() < (this.startTick + this.invoDelay)) return;

        this.stopIt();

        this.startTick = 0;

        if(this.preModifySound != null) this.preModifySound.modify(this);
        getSoundHandler().play(this);
    }

    private long currentTime() {
        if (this.savedWorld != ClientUtil.getWorld()) {
            this.savedWorld = ClientUtil.getWorld();
            this.stopIt();
        }

        return this.savedWorld.getGameTime();
    }

    public void stopIt(){
        getSoundHandler().stop(this);
//        if (this.mySource == null) return;
//        this.mySource.stop();
//        this.mySource = null;
        this.startTick = this.delayedStart ? 0 : -1;
    }

    @FunctionalInterface
    public interface IModifySound {
        void modify(InvoSound sound);
    }
}
