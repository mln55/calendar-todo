package com.personalproject.todo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.personalproject.todo.vo.Todo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TodoDaoImpl implements TodoDao {

    private final String NAME_SPACE = "com.personalproject.todo.mapper.TodoMapper";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<Todo> todoByMemberThisMonth(String mId, String firstDate, String lastDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mId", mId);
        paramMap.put("firstDate", firstDate);
        paramMap.put("lastDate", lastDate);
        return sqlSession.selectList(NAME_SPACE + ".todoByMemberThisMonth", paramMap);
    }

    @Override
    public List<Todo> todoByMemberSelectedDate(String mId, String selectedDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mId", mId);
        paramMap.put("selectedDate", selectedDate);

        return sqlSession.selectList(NAME_SPACE + ".todoByMemberSelectedDate", paramMap);
    }

    @Override
    public int insertTodo(Todo todo) {
        return sqlSession.insert(NAME_SPACE + ".insertTodo", todo);
    }

    @Override
    public int modifyTodo(Todo todo) {
        return sqlSession.update(NAME_SPACE + ".modifyTodo", todo);
    }

    @Override
    public int deleteTodo(int no) {
        return sqlSession.delete(NAME_SPACE + ".deleteTodo", no);
    }

    @Override
    public int changeTodoColor(int no, String color) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("no", no);
        paramMap.put("color", color);
        return sqlSession.update(NAME_SPACE + ".changeTodoColor", paramMap);
    }

    @Override
    public Todo membersTodo(String mId, int no) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mId", mId);
        paramMap.put("no", no);

        return sqlSession.selectOne(NAME_SPACE + ".membersTodo", paramMap);
    }

    @Override
    public List<Todo> todoByMemberWithSearch(String mId, String firstDate, String lastDate, String include, String ignore, String target, String sort) {

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
}
