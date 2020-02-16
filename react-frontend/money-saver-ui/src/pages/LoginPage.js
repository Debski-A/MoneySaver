import React, { Component } from 'react';
import SubmitButton from '../components/SubmitButton'
import Alert from 'react-bootstrap/Alert'
import Cookies from 'js-cookie'
import Form from 'react-bootstrap/Form'
import Col from "react-bootstrap/Col"
import Row from "react-bootstrap/Row"
import i18n from '../helpers/i18n'
import { withTranslation } from "react-i18next";

class LoginPage extends Component {
    state = {
        username: '',
        password: '',
        message: ''
    }

    handleOnClick = () => {
        let apiBaseUrl = "http://localhost:80/api/auth/oauth/token"
        let accessTokenCredentials = new FormData()
        let currentLanguage = i18n.language
        accessTokenCredentials.append('grant_type', 'password')
        accessTokenCredentials.append('username', this.state.username)
        accessTokenCredentials.append('password', this.state.password)
        accessTokenCredentials.append('scope', 'ui')
        let basicAuthResult = btoa("browser:")
        fetch(apiBaseUrl, {
            method: 'POST',
            body: accessTokenCredentials,
            headers: {
                'Authorization': 'Basic ' + basicAuthResult,
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => {
                if (!!response.errorMessage) {
                    this.setState({
                        message: response.errorMessage
                    })
                    setTimeout(() => {
                        this.setState({
                            message: ''
                        });
                      }, 2000);
                }
                else if (!!response.error) {
                    this.setState({
                        message: this.props.t('login_error')
                    })
                    setTimeout(() => {
                        this.setState({
                            message: ''
                        });
                      }, 2000);
                }
                else {
                    let { access_token, refresh_token } = response
                    Cookies.set("access_token", access_token)
                    Cookies.set("refresh_token", refresh_token)
                    this.props.handleIsLoggedIn()
                    this.props.history.push({
                        pathname: "/main"
                    })
                }
            })
            .catch((err) => {
                this.setState({
                    message: this.props.t('login_error')
                })
                setTimeout(() => {
                    this.setState({
                        message: ''
                    });
                  }, 2000);
            });
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
            <Form className="mt-2">
                <Form.Group as={Row}>
                    <Col sm={5} >
                        <Form.Control id="username_input" onChange={this.handleOnChange} placeholder={this.props.t('username_prompt')} />
                    </Col>
                </Form.Group>
                <Form.Group as={Row}>
                    <Col sm={5} >
                        <Form.Control id="password_input" onChange={this.handleOnChange} type="password" placeholder={this.props.t('password_prompt')} />
                    </Col>
                </Form.Group >
                <Alert show={!!this.state.message} variant="danger">
                    {this.state.message}
                </Alert>
                <SubmitButton onClick={this.handleOnClick} value={this.props.t('submit_button')} />
            </Form>
        );
    }

}

export default withTranslation('login_page')(LoginPage);