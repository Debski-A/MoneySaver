package com.debski.accountservice.utils;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.enums.Role;
import com.debski.accountservice.models.AccountDTO;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class TransformationUtilsUnitTest {

    private TransformationUtils transformationUtils = new TransformationUtils();


    @Test
    public void shouldMapEntityToDto() {
        //given
        Account entity = Account.builder().username("user").password("Password1").email("xyz@gmail.com").build();
        //when
        AccountDTO accountDTO = transformationUtils.entityToDto(entity);
        //then
        assertThat(accountDTO.getUsername(), is(entity.getUsername()));
        assertThat(accountDTO.getRawPassword(), is(nullValue()));
        assertThat(accountDTO.getEmail(), is(entity.getEmail()));
    }

    @Test
    public void shouldReturnNull() {
        //when
        AccountDTO accountDTO = transformationUtils.entityToDto(null);
        //then
        assertThat(accountDTO, is(nullValue()));
    }

    @Test
    public void shouldMapDtoToEntity() {
        //given
        AccountDTO dto = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").build();
        //when
        Account entity = transformationUtils.dtoToEntity(dto);
        //then
        assertThat(entity.getUsername(), is(dto.getUsername()));
        assertThat(entity.getEmail(), is(dto.getEmail()));
        assertThat(entity.getRole(), is(Role.USER));
    }


}
