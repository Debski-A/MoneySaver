import React, { Component } from "react";
import TextField from "../components/TextField";
import SubmitButton from "../components/SubmitButton";
//import SubmitButton from "../components/SubmitButton";

class RegisterPage extends Component {
  state = {
    username: '',
    password: '',
    email: '',
    message: ''
  }

  handleOnChange = (e) => {
    console.log(e.target.id)
    switch (e.target.id) {
      case "username_input": {
        this.setState({
          username: e.target.value
        })
        break;
      }
      case "password_input": {
        this.setState({
          password: e.target.value
        })
        break;
      }
      case "email_input": {
        this.setState({
          email: e.target.value
        })
        break;
      }
      default: break;
    }
  }

  handleOnClick = () => {
    let apiBaseUrl = "http://localhost/api/accounts/";
    let registerCredentials = {
      "password": this.state.password,
      "username": this.state.username,
      "email": this.state.email
    }
    console.log(registerCredentials)
    fetch(apiBaseUrl + 'create', {
      method: "POST",
      body: JSON.stringify(registerCredentials),
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
      .then(response => response.json())
      .then(response => {
        if (!!response.errorMessage) {
          this.setState({
            message: response.errorMessage
          }
          )
        } else {
          this.props.history.push("/login")
        }

      })
      .catch((err) => this.setState({
        message: err
      }));
  }

  render() {
    console.log(this.state.message)
    return (
      <div>
        <span>username</span>
        <TextField id="username_input" onChange={this.handleOnChange} />
        <span>password</span>
        <TextField id="password_input" onChange={this.handleOnChange} />
        <span>email</span>
        <TextField id="email_input" onChange={this.handleOnChange} />
        <SubmitButton onClick={this.handleOnClick} value="Submit"/>
        <span>{this.state.message}</span>
      </div>
    );
  }
}

export default RegisterPage;