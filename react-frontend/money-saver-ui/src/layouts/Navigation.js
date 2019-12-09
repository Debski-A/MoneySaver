import React from 'react';
import { NavLink } from 'react-router-dom';

const list = [
  { name: "start", path: "/", exact: true },
  { name: "rejestracja", path: "/register" },
  { name: "logowanie", path: "/login" },
  { name: "aplikacja", path: "/main" }
]

const Navigation = (props) => {

  const menu = list.map(item => (
    <li key={item.name}>
      <NavLink to={item.path} exact={item.exact ? item.exact : false}>{item.name}</NavLink>
    </li>
  ))

  const menu2 = (
    <>
      <li>
        <NavLink to='/' exact>start</NavLink>
      </li>
      <li>
        <NavLink to='/register'>rejestracja</NavLink>
      </li>
      {props.isLoggedIn ? (
        <li>
        <NavLink to='/logout' >wyloguj</NavLink>
      </li>
      ) : (
        <li>
        <NavLink to='/login' >logowanie</NavLink>
      </li>
      )}
      <li>
        <NavLink to='/main' exact>aplikacja</NavLink>
      </li>
    </>
  )

  return (
    <nav className="main">
      <ul>
        {menu2}
      </ul>
    </nav>
  );
}

export default Navigation;