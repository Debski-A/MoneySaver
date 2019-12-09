import React, { Component } from 'react';
import TextField from '../components/TextField'
import SubmitButton from '../components/SubmitButton'
import Cookies from 'js-cookie'

class LoginPage extends Component {
    state = {
        username: '',
        password: '',
        message: ''
    }

    handleOnClick = () => {
        let apiBaseUrl = "http://localhost:80/api/auth/oauth/token";
        let accessTokenCredentials = new FormData();
        accessTokenCredentials.append('grant_type', 'password')
        accessTokenCredentials.append('username', this.state.username)
        accessTokenCredentials.append('password', this.state.password)
        accessTokenCredentials.append('scope', 'ui')
        let basicAuthResult = btoa("browser:")
        fetch(apiBaseUrl, {
            method: 'POST',
            body: accessTokenCredentials,
            headers: {
                'Authorization': 'Basic ' + basicAuthResult
            }
        })
            .then(response => {
                let responseJson = response.json()
                if (response.status === 200) {
                    let { access_token, refresh_token } = responseJson
                    Cookies.set("access_token", access_token)
                    Cookies.set("refresh_token", refresh_token)
                    this.props.handleIsLoggedIn()
                    this.props.history.push("/")
                } else {
                    this.setState({
                        message: responseJson.error_description
                    })
                }
            })
            .catch((err) => this.setState({
                message: err
            }));
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
            default: break;
        }
    }
    render() {
        return (
            <div>
                <span>username</span>
                <TextField id="username_input" onChange={this.handleOnChange} />
                <span>password</span>
                <TextField id="password_input" onChange={this.handleOnChange} />
                <SubmitButton onClick={this.handleOnClick} value="Submit" />
                <span>{this.state.message}</span>
            </div>
        );
    }

}

export default LoginPage;