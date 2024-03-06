package com.example.namo2.domain.user.application.converter;

import com.example.namo2.domain.user.domain.Content;
import com.example.namo2.domain.user.domain.Term;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.user.ui.dto.UserRequest;

import java.util.ArrayList;
import java.util.List;

public class TermConverter {
    public static List<Term> toTerms(UserRequest.TermDto termDto, User user) {
        List<Term> terms = new ArrayList<>();
        System.out.println("termOfUse" + termDto.getIsCheckTermOfUse());
        System.out.println("termOfUse" + termDto.getIsCheckPersonalInformationCollection());
        Term termOfUse = toTerm(Content.TERMS_OF_USE, termDto.getIsCheckTermOfUse(), user);
        Term termOfPersonal
                = toTerm(Content.PERSONAL_INFORMATION_COLLECTION, termDto.getIsCheckPersonalInformationCollection(), user);
        terms.add(termOfUse);
        terms.add(termOfPersonal);
        return terms;
    }

    private static Term toTerm(Content content, Boolean isCheck, User user) {
        return Term.builder()
                .user(user)
                .content(content)
                .isCheck(isCheck)
                .build();
    }
}
