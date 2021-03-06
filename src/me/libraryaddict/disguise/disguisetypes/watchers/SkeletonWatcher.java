package me.libraryaddict.disguise.disguisetypes.watchers;

import org.bukkit.entity.Skeleton.SkeletonType;

import me.libraryaddict.disguise.disguisetypes.Disguise;

/**
 * @author Navid
 */
public class SkeletonWatcher extends LivingWatcher {

    public SkeletonWatcher(Disguise disguise) {
        super(disguise);
    }

    public void setType(SkeletonType type) {
        setValue(11, type.getId());
        sendData(11);
    }

    public SkeletonType getType() {
        return SkeletonType.getType((int) getValue(11, SkeletonType.NORMAL.getId()));
    }
}
