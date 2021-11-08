package com.personalproject.todo.member;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import lombok.Getter;

@Getter
public class Member {
    private String id;
    private String email;
    private String password;
    private LocalDateTime regDate;
    private Auth auth;

    public Member(String id, String email, String password, LocalDateTime regDate, Auth auth) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.regDate = regDate;
        this.auth = auth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return ToStringBuilder
            .reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .toString();
    }

    public static class Builder {
        private String id;
        private String email;
        private String password;
        private LocalDateTime regDate;
        private Auth auth;

        public Builder() { }

        public Builder(Member source) {
            BeanUtils.copyProperties(source, this);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder regDate(LocalDateTime regDate) {
            this.regDate = regDate;
            return this;
        }

        public Builder auth(Auth auth) {
            this.auth = auth;
            return this;
        }

        public Member build() {
            return new Member(
                id,
                email,
                password,
                regDate,
                auth
            );
        }
    }
}
