package com.personalproject.todo.todo;

import static com.personalproject.todo.utils.DateTimeUtils.stringDateToLocalDate;

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
public class TodoDto {

    private int no;
    private String mId;
    private String title;
    private String content;
    private LocalDate beginDate;
    private LocalDate dueDate;
    private LocalTime beginTime;
    private LocalTime dueTime;
    private int status;
    private String color;
    private int dDay;
    private int barLevel;

    public TodoDto() { }

    public TodoDto(Todo source) {
        // dDay, barLevel must be provided in TodoService
        String[] ignores = {"beginDate", "dueDate"};
        BeanUtils.copyProperties(source, this, ignores);

        LocalDateTime bd = source.getBeginDate();
        LocalDateTime dd = source.getDueDate();
        beginDate = bd.toLocalDate();
        dueDate = dd.toLocalDate();
        beginTime = bd.toLocalTime();
        dueTime = dd.toLocalTime();
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public void setBeginDate(String beginDateString) {
        this.beginDate = stringDateToLocalDate(beginDateString);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDueDate(String dueDateString) {
        this.dueDate = stringDateToLocalDate(dueDateString);
    }

    public LocalTime getBeginTime() {
        return beginTime == null ? LocalTime.of(0, 0, 0) : beginTime;
    }

    public LocalTime getDueTime() {
        return dueTime == null ? LocalTime.of(0, 0, 0) : dueTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder
            .reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .toString();
    }
}
