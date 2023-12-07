package com.sparta.backoffice.follow.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class FollowID implements Serializable {

    private Long followerId;
    private Long followingId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof FollowID )) return false;
        FollowID followID = (FollowID) obj;

        return Objects.equals(followerId, followID.followerId) &&
                Objects.equals(followingId, followID.followingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followingId);
    }
}
