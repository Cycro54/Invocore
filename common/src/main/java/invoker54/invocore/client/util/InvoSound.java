package invoker54.invocore.client.util;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3d;

public class InvoSound extends AbstractSoundInstance {
    private static final Logger LOGGER = LogManager.getLogger();
    private long startTick = -1;
    private int invoDelay = 0;
    private boolean delayedStart = false;

    private IModifySound preModifySound;

    private final SoundEvent mySoundEvent;
    private final SoundSource mySoundCategory;

    private static SoundManager soundManager;
    private Level savedWorld = null;


    public InvoSound(SoundEvent soundIn, SoundSource categoryIn) {
        super(soundIn, categoryIn, SoundInstance.createUnseededRandom());
        this.mySoundEvent = soundIn;
        this.mySoundCategory = categoryIn;
    }

    public static SoundManager getSoundHandler(){
        if (soundManager == null) soundManager = ClientUtil.getMinecraft().getSoundManager();
        return soundManager;
    }

    public boolean isDonePlaying() {
//        return this.mySource == null || this.mySource.isStopped();
        return getSoundHandler().isActive(this) == false;
    }

    public InvoSound duplicate(){
        return new InvoSound(this.mySoundEvent, this.mySoundCategory).setPreModifySound(this.preModifySound)
                .setVolume(this.volume).setPitch(this.pitch).setPos(new Vector3d(this.x, this.y, this.z))
                .setRepeatDelay(this.invoDelay, this.delayedStart).setAttenuation(this.attenuation)
                .setGlobal(this.relative);
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
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        return this;
    }

    public InvoSound setRepeatDelay(int delayInTicks, boolean delayedStart){
        this.invoDelay = delayInTicks;
        this.delayedStart = delayedStart;
        return this;
    }

    public InvoSound setAttenuation(Attenuation type){
        this.attenuation = type;
        return this;
    }

    public InvoSound setGlobal(boolean relative){
        this.relative = relative;
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
