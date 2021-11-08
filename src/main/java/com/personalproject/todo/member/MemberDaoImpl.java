package com.personalproject.todo.member;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDaoImpl implements MemberDao {

    private final String NAME_SPACE = "com.personalproject.todo.mapper.MemberMapper";

    private final SqlSession sqlSession;

    public MemberDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Member findMember(String id) {
        return sqlSession.selectOne(NAME_SPACE + ".findMember", id);
    }

    @Override
    public String findId(String id) {
        return sqlSession.selectOne(NAME_SPACE + ".findId", id);
    }

    @Override
    public int insertMember(Member member) {
        return sqlSession.insert(NAME_SPACE + ".insertMember", member);
    }
}
