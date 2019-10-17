import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import React, { Component } from 'react'

class Register extends Component {

  state = {
    username: '',
    password: '',
    email: ''
  }

  handleOnChange = (e) => {
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

  handleClick = () => {
    let apiBaseUrl = "http://localhost:6000/accounts/";
    let registerCredentials = {
      "password": this.state.password,
      "username": this.state.username,
      "email": this.state.email
    }
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
        console.log(response);
        if (response.status === 200) {
          console.log("cool, user was created")
        }
        else if (response.status === 500) {
          console.log(response.data.errorMessage);
        }
      })
      .catch((err) => console.log(err));
  }

  render() {
    return (
      <div>
        <MuiThemeProvider>
          <div>
            <AppBar
              title="Register"
            />
            <TextField
              id="username_input"
              hintText="Enter your Username"
              floatingLabelText="Username"
              onChange={this.handleOnChange}
            />
            <br />
            <TextField
              id="password_input"
              type="password"
              hintText="Enter your Password"
              floatingLabelText="Password"
              onChange={this.handleOnChange}
            />
            <br />
            <TextField
              id="email_input"
              type="email"
              hintText="Enter your Email"
              floatingLabelText="Email"
              onChange={this.handleOnChange}
            />
            <br />
            <RaisedButton label="Submit" primary={true} style={style} onClick={this.handleClick} />
          </div>
        </MuiThemeProvider>
      </div>
    );
  }
}
const style = {
  margin: 15,
};
export default Register;