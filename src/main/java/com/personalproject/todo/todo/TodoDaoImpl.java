package com.personalproject.todo.todo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class TodoDaoImpl implements TodoDao {

    private final String NAME_SPACE = "com.personalproject.todo.mapper.TodoMapper";

    private final SqlSession sqlSession;

    public TodoDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Todo> todoByMemberThisMonth(String mId, LocalDate firstDate, LocalDate lastDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mId", mId);
        paramMap.put("firstDate", firstDate);
        paramMap.put("lastDate", lastDate);

        return sqlSession.selectList(NAME_SPACE + ".todoByMemberThisMonth", paramMap);
    }

    public List<Todo> todoByMemberWithSearch(String mId, LocalDate firstDate, LocalDate lastDate, String include, String ignore, String target, String sort) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mId", mId);
        paramMap.put("firstDate", firstDate);
        paramMap.put("lastDate", lastDate);
        paramMap.put("include", include);
        paramMap.put("ignore", ignore);
        paramMap.put("target", target);
        paramMap.put("sort", sort);

        return sqlSession.selectList(NAME_SPACE + ".todoByMemberWithSearch", paramMap);
    }

    public List<Todo> todoByMemberSelectedDate(String mId, LocalDate selectedDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mId", mId);
        paramMap.put("selectedDate", selectedDate);

        return sqlSession.selectList(NAME_SPACE + ".todoByMemberSelectedDate", paramMap);
    }

    public int insertTodo(Todo todo) {
        return sqlSession.insert(NAME_SPACE + ".insertTodo", todo);
    }

    public int modifyTodo(Todo todo) {
        return sqlSession.insert(NAME_SPACE + ".modifyTodo", todo);
    }

    public int deleteTodo(Integer no) {
        return sqlSession.insert(NAME_SPACE + ".deleteTodo", no);
    }

    public int changeTodoColor(Integer no, String color) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("no", no);
        paramMap.put("color", color);

        return sqlSession.update(NAME_SPACE + ".changeTodoColor", paramMap);
    }

    public Todo membersTodo(Integer no, String mId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("no", no);
        paramMap.put("mId", mId);

        return sqlSession.selectOne(NAME_SPACE + ".membersTodo", paramMap);
    }
}
