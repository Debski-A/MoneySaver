import React from 'react';
import { NavLink } from 'react-router-dom';

const list = [
  { name: "start", path: "/", exact: true },
  { name: "rejestracja", path: "/register" },
  { name: "logowanie", path: "/login" },
  { name: "aplikacja", path: "/main" }
]

const Navigation = () => {

  const menu = list.map(item => (
    <li key={item.name}>
      <NavLink to={item.path} exact={item.exact ? item.exact : false}>{item.name}</NavLink>
    </li>
  ))

  return (
    <nav className="main">
      <ul>
        {menu}
      </ul>
    </nav>
  );
}

export default Navigation;