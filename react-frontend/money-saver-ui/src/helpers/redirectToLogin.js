import { withRouter } from "react-router-dom";
import React from "react";
import { Redirect } from 'react-router-dom'

const RedirectToLogin = (props) => {
    props.history.push("/login")
}

export default withRouter(RedirectToLogin)