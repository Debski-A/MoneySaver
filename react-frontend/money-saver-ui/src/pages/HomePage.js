import React, { Component } from 'react';
import Typical from 'react-typical'
import Col from "react-bootstrap/Col"
import Row from "react-bootstrap/Row"
import Container from "react-bootstrap/Container"
import { withTranslation } from "react-i18next";

class HomePage extends Component {

    _timeOfUpdate = Date.now();

    shouldComponentUpdate(nextProps) {
        if ((Date.now() - this._timeOfUpdate) < 14000) {
            // console.log('Wynik: ' + (Date.now() - this._timeOfUpdate))
            return false;
        }
        this._timeOfUpdate = Date.now()
        return true;
    }

    render() {
        return (
            <Container fluid className="d-flex flex-column fill-height">
                <Row className="fill-height">
                    <Col className="text-center my-auto">
                        <Typical
                            steps={[1000, this.props.t('text1'), 1500, this.props.t('text2'), 3000, this.props.t('text3')]}
                            loop={1}
                            wrapper="h1"
                        />
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default withTranslation('homepage')(HomePage);