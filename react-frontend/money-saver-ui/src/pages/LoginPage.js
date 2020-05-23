import React, { Component } from "react";
import SubmitButton from "../components/SubmitButton";
import Alert from "react-bootstrap/Alert";
import Cookies from "js-cookie";
import Form from "react-bootstrap/Form";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import i18n from "../helpers/i18n";
import { withTranslation } from "react-i18next";

class LoginPage extends Component {
  state = {
    username: "",
    password: "",
    message: "",
  };

  handleOnClick = () => {
    let apiBaseUrl = "http://localhost:80/api/auth/oauth/token";
    let accessTokenCredentials = new FormData();
    let currentLanguage = i18n.language;
    accessTokenCredentials.append("grant_type", "password");
    accessTokenCredentials.append("username", this.state.username);
    accessTokenCredentials.append("password", this.state.password);
    accessTokenCredentials.append("scope", "ui");
    let basicAuthResult = btoa("browser:");
    fetch(apiBaseUrl, {
      method: "POST",
      body: accessTokenCredentials,
      headers: {
        Authorization: "Basic " + basicAuthResult,
        "Accept-Language": currentLanguage,
      },
    })
      .then((response) => {
        if (!!response.ok) {
          response.json().then((result) => {
            console.log("Success");
            let { access_token, refresh_token } = result;
            Cookies.set("access_token", access_token);
            Cookies.set("refresh_token", refresh_token);
            this.props.handleIsLoggedIn();
            this.props.history.push({
              pathname: "/main",
            });
          });
        } else {
          response.json().then((result) => {
            console.log("Fail");
            let message = result.errorMessage;
            this.setState({
              message: message,
            });
            setTimeout(() => {
              this.setState({
                message: "",
              });
            }, 2000);
          });
        }
      })
      .catch((err) => {
        console.log(err);
        let message = this.props.t("login_error");
        this.setState({
          message: message,
        });
        setTimeout(() => {
          this.setState({
            message: "",
          });
        }, 2000);
      });
  };

  handleOnChange = (e) => {
    switch (e.target.id) {
      case "username_input": {
        this.setState({
          username: e.target.value,
        });
        break;
      }
      case "password_input": {
        this.setState({
          password: e.target.value,
        });
        break;
      }
      default:
        break;
    }
  };
  render() {
    return (
      <Form className="mt-2 ml-2 w-100">
        <Form.Group as={Row}>
          <Col sm={4}>
            <Form.Control
              id="username_input"
              onChange={this.handleOnChange}
              placeholder={this.props.t("username_prompt")}
            />
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Col sm={4}>
            <Form.Control
              id="password_input"
              onChange={this.handleOnChange}
              type="password"
              placeholder={this.props.t("password_prompt")}
            />
          </Col>
        </Form.Group>
        <SubmitButton
          onClick={this.handleOnClick}
          value={this.props.t("submit_button")}
        />
        <Row className="pt-2">
          <Col sm={4}>
            <Alert show={!!this.state.message} variant="danger">
              {this.state.message}
            </Alert>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default withTranslation("login_page")(LoginPage);
