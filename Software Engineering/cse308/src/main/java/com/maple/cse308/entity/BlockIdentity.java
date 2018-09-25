/*
package com.maple.cse308.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BlockIdentity implements Serializable {

    @Column(name="blocker")
    private Integer blocker;

    @Column(name="blocking")
    private Integer blocking;

    public BlockIdentity(){

    }

    public BlockIdentity(Integer blocker, Integer blocking){
        this.blocker = blocker;
        this.blocking = blocking;
    }

    public Integer getBlocker() {
        return blocker;
    }

    public void setBlocker(Integer blocker) {
        this.blocker = blocker;
    }

    public Integer getBlocking() {
        return blocking;
    }

    public void setBlocking(Integer blocking) {
        this.blocking = blocking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockIdentity that = (BlockIdentity) o;

        if (blocker != null ? !blocker.equals(that.blocker) : that.blocker != null) return false;
        return blocking != null ? blocking.equals(that.blocking) : that.blocking == null;
    }

    @Override
    public int hashCode() {
        int result = blocker != null ? blocker.hashCode() : 0;
        result = 31 * result + (blocking != null ? blocking.hashCode() : 0);
        return result;
    }
}
*/
