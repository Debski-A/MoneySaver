import React, { Component } from 'react'
import Button from 'react-bootstrap/Button'
import Row from "react-bootstrap/Row"
import Col from "react-bootstrap/Col"
import Dropdown from "react-bootstrap/Dropdown"
import DropdownButton from "react-bootstrap/DropdownButton"
import Container from 'react-bootstrap/Container'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPiggyBank } from '@fortawesome/free-solid-svg-icons'
import { faMoneyBillWave } from '@fortawesome/free-solid-svg-icons'
import { faChartLine } from '@fortawesome/free-solid-svg-icons'
import HomePageContent from '../layouts/HomePageContent'
import { NavLink } from 'react-router-dom'
import { withTranslation } from "react-i18next"
import { registerLocale } from 'react-datepicker'
import pl from "date-fns/locale/pl";
import { authenticate, isAuthenticated } from '../helpers/authenticationUtils'
registerLocale('pl', pl)

class MainPage extends Component {
    render() {
        if (!isAuthenticated) authenticate()
        return (
            <Container fluid className="d-flex fill-height">
                <Col sm={2} className="d-flex fill-height flex-column">
                    <Row className="fill-height align-items-center">
                        <FontAwesomeIcon size="4x" icon={faPiggyBank} color="#6c757d" />
                        <Button as={NavLink} className="ml-2" variant="secondary" to="/main/income/add" >{this.props.t('income_button')}</Button>
                    </Row>
                    <Row className="fill-height align-items-center">
                        <FontAwesomeIcon size="4x" icon={faMoneyBillWave} color="#6c757d" />
                        <Button as={NavLink} className="ml-2" variant="secondary" to="/main/outcome/add">{this.props.t('outcome_button')}</Button>
                    </Row>
                    <Row className="fill-height align-items-center">
                        <FontAwesomeIcon size="4x" icon={faChartLine} color="#6c757d" />
                        {/* <Button className="ml-2" variant="secondary" to="/main/visualization/1">{this.props.t('visualisation_button')}</Button> */}
                        <DropdownButton className="ml-2" variant="secondary" title={this.props.t('visualisation_button')}>
                            <Dropdown.Item as={NavLink} variant="secondary" to="/main/visualization/1">Action</Dropdown.Item>
                            {/* <Dropdown.Item as={NavLink} variant="secondary" to="/main/visualization/2">Action</Dropdown.Item>
                            <Dropdown.Item as={NavLink} variant="secondary" to="/main/visualization/3">Action</Dropdown.Item> */}
                        </DropdownButton>
                    </Row>
                </Col>
                <Col sm={10}>
                    <HomePageContent />
                </Col>
            </Container>
        )
    }
}

export default withTranslation('mainpage')(MainPage)
