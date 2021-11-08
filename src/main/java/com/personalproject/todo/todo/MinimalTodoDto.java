package com.personalproject.todo.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinimalTodoDto {

    private String title;
    private String content;
    private LocalDate beginDate;
    private LocalDate dueDate;
    private LocalTime beginTime;
    private LocalTime dueTime;
    private String color;

    public MinimalTodoDto(Todo source) {
        String[] ignores = {"beginDate", "dueDate"};
        BeanUtils.copyProperties(source, this, ignores);

        LocalDateTime bd = source.getBeginDate();
        LocalDateTime dd = source.getDueDate();
        beginDate = bd.toLocalDate();
        dueDate = dd.toLocalDate();
        beginTime = bd.toLocalTime();
        dueTime = dd.toLocalTime();
    }

    public MinimalTodoDto(TodoDto source) {
        BeanUtils.copyProperties(source, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder
            .reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .toString();
    }
}
