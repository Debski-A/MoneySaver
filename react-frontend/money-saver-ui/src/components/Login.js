import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import React, { Component } from 'react'

class Login extends Component {

  state = {
    username: '',
    password: '',
  }

  handleClick(event) {
    var apiBaseUrl = "http://localhost:4000/api/";
    var loginCredentials = {
      "password": this.state.password,
      "username": this.state.username
    }
    fetch.post(apiBaseUrl + 'login', loginCredentials)
      .then(function (response) {
        console.log(response);
        if (response.data.code === 200) {
          // console.log("Login successfull"); 
          // var uploadScreen = [];
          // uploadScreen.push(<UploadScreen appContext={self.props.appContext} />)
          // self.props.appContext.setState({ loginPage: [], uploadScreen: uploadScreen })
        }
        else {
          console.log("Username does not exists");
          alert("Username does not exist");
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  }


  render() {
    return (
      <div>
        <MuiThemeProvider>
          <div>
            <AppBar
              title="Login"
            />
            <TextField
              hintText="Enter your Username"
              floatingLabelText="Username"
              onChange={(newValue) => this.setState({ username: newValue })}
            />
            <br />
            <TextField
              type="password"
              hintText="Enter your Password"
              floatingLabelText="Password"
              onChange={(newValue) => this.setState({ password: newValue })}
            />
            <br />
            <RaisedButton label="Submit" primary={true} style={style} onClick={(event) => this.handleClick(event)} />
          </div>
        </MuiThemeProvider>
      </div>
    );
  }
}
const style = {
  margin: 15,
};
export default Login;