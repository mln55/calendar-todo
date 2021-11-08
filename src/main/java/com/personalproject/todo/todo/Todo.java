package com.personalproject.todo.todo;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;

@Getter
public class Todo {
    private Integer no;
    private String mId;
    private String title;
    private String content;
    private LocalDateTime beginDate;
    private LocalDateTime dueDate;
    private int status;
    private String color;

    public Todo(Integer no, String mId, String title, String content, LocalDateTime beginDate, LocalDateTime dueDate, int status, String color) {
        this.no = no;
        this.mId = mId;
        this.title = title;
        this.content = content;
        this.beginDate = beginDate;
        this.dueDate = dueDate;
        this.status = status;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(no, todo.no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no);
    }

    @Override
    public String toString() {
        return ToStringBuilder
            .reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .toString();
    }

    public static class Builder {
        private Integer no;
        private String mId;
        private String title;
        private String content;
        private LocalDateTime beginDate;
        private LocalDateTime dueDate;
        private int status;
        private String color;

        public Builder() { }

        public Builder(Todo todo) {
            this.no = todo.no;
            this.mId = todo.mId;
            this.title = todo.title;
            this.content = todo.content;
            this.beginDate = todo.beginDate;
            this.dueDate = todo.dueDate;
            this.status = todo.status;
            this.color = todo.color;
        }

        public Builder no(Integer no) {
            this.no = no;
            return this;
        }

        public Builder mId(String mId) {
            this.mId = mId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder beginDate(LocalDateTime beginDate) {
            this.beginDate = beginDate;
            return this;
        }

        public Builder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Todo build() {
            return new Todo(
                no,
                mId,
                title,
                content,
                beginDate,
                dueDate,
                status,
                color
            );
        }
    }
}
