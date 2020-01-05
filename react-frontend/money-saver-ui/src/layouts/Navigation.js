import React from 'react'
import { NavLink } from 'react-router-dom'
import { } from 'react-router-dom'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'
import { useTranslation} from "react-i18next";
import i18n from '../helpers/i18n'

const changeLanguage = lng => {
  i18n.changeLanguage(lng);
};

const Navigation = (props) => {
  const { t } = useTranslation('navigation');

  return (
      <Nav fill variant="pills" className="bg-light text-dark" >
        <Nav.Item>
          {props.isLoggedIn ? (
            <Nav.Link as={NavLink} exact to='/main' className="bg-light text-dark">{t('application')}</Nav.Link>
          ) : (
              <Nav.Link as={NavLink} exact to='/' className="bg-light text-dark">{t('start')}</Nav.Link>
            )}
        </Nav.Item>
        <Nav.Item>
          <Nav.Link as={NavLink} exact to='/register' className="bg-light text-dark">{t('registration')}</Nav.Link>
        </Nav.Item>
        <Nav.Item>
          {props.isLoggedIn ? (
            <Nav.Link as={NavLink} exact to='/logout' className="bg-light text-dark">{t('logout')}</Nav.Link>
          ) : (
              <Nav.Link as={NavLink} exact to='/login' className="bg-light text-dark">{t('login')}</Nav.Link>
            )}
        </Nav.Item>
        <NavDropdown bsPrefix="nav-link bg-light text-dark" title={t('lang')} >
          <NavDropdown.Item className="bg-light text-dark" onClick={() => changeLanguage("pl")}>PL</NavDropdown.Item>
          <NavDropdown.Divider />
          <NavDropdown.Item className="bg-light text-dark" onClick={() => changeLanguage("en")}>EN</NavDropdown.Item>
        </NavDropdown>
      </Nav>
  );
}

export default Navigation;