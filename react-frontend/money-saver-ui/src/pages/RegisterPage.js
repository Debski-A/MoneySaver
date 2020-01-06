import React, { Component } from "react"
import SubmitButton from "../components/SubmitButton"
import Form from 'react-bootstrap/Form'
import Alert from 'react-bootstrap/Alert'
import Col from "react-bootstrap/Col"
import Row from "react-bootstrap/Row"
import i18n from '../helpers/i18n'
import { withTranslation } from "react-i18next";

class RegisterPage extends Component {
  state = {
    username: '',
    password: '',
    email: '',
    message: ''
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

  handleOnClick = () => {
    let apiBaseUrl = "http://localhost:80/api/accounts/create"
    let currentLanguage = i18n.language
    let registerCredentials = {
      "password": this.state.password,
      "username": this.state.username,
      "email": this.state.email
    }
    fetch(apiBaseUrl, {
      method: 'POST',
      body: JSON.stringify(registerCredentials),
      headers: {
        'Content-Type': 'application/json',
        'Accept-Language': currentLanguage
      }
    })
      .then(response => response.json())
      .then(response => {
        if (!!response.errorMessage) {
          this.setState({
            message: response.errorMessage
          })
        }
        else {
          this.props.history.push("/login")
        }
      })
      .catch((err) => {
        console.log(err)
        this.setState({
          message: this.props.t('register_error')
        })
      });
  }

  render() {
    return (
      <Form className="mt-2">
        <Form.Group as={Row}>
          <Col sm={5}>
            <Form.Control id="username_input" onChange={this.handleOnChange} placeholder={this.props.t('username_prompt')} />
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Col sm={5} >
            <Form.Control id="password_input" onChange={this.handleOnChange} type="password" placeholder={this.props.t('password_prompt')} />
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Col sm={5}>
            <Form.Control id="email_input" onChange={this.handleOnChange} type="email" placeholder={this.props.t('email_prompt')} />
          </Col>
        </Form.Group>
        <Alert show={!!this.state.message} variant="danger">
          {this.state.message}
        </Alert>
        <SubmitButton onClick={this.handleOnClick} value={this.props.t('submit_button')} />
      </Form>
    );
  }
}

export default withTranslation('register_page')(RegisterPage);