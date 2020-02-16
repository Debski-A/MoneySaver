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
import Loader from 'react-loader'
import pl from "date-fns/locale/pl"
import i18n from '../helpers/i18n'
import { getAccessToken } from '../helpers/authenticationUtils'
registerLocale('pl', pl)

class MainPage extends Component {

    state = {
        currencies: [],
        frequencies: [],
        incomeCategories: [],
        outcomeCategories: [],
        isDataLoaded: false,
        currentLanguage: i18n.language
    }

    componentDidMount() {
        this.getDropdownValues()
    }

    getDropdownValues = async () => {
        // if (!isAuthenticated) authenticate()

        let dropDownValuesURL = "http://localhost/api/accounts/dropdown_values"
        let currentLanguage = i18n.language
        await fetch(dropDownValuesURL, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => {
                let { currencies, frequencies, incomeCategories, outcomeCategories } = response
                this.setState({
                    currencies: currencies,
                    frequencies: frequencies,
                    incomeCategories: incomeCategories,
                    outcomeCategories: outcomeCategories,
                    isDataLoaded: true
                })
            })
            .catch(err => {
                console.log(err)
            })
    }

    render() {
        if (!this.state.isDataLoaded) return <Loader/>
        return (
            <Container fluid className="d-flex fill-height">
                <Col sm={1} className="d-flex fill-height flex-column">
                    <Row className="fill-height align-items-center">
                        {/* <FontAwesomeIcon size="4x" icon={faPiggyBank} color="#6c757d" /> */}
                        <Button as={NavLink} className="ml-1" variant="secondary" to="/main/income/add" >{this.props.t('income_button')}</Button>
                    </Row>
                    <Row className="fill-height align-items-center">
                        {/* <FontAwesomeIcon size="4x" icon={faMoneyBillWave} color="#6c757d" /> */}
                        <Button as={NavLink} className="ml-1" variant="secondary" to="/main/outcome/add">{this.props.t('outcome_button')}</Button>
                    </Row>
                    <Row className="fill-height align-items-center">
                        {/* <FontAwesomeIcon size="4x" icon={faChartLine} color="#6c757d" /> */}
                        {/* <Button className="ml-2" variant="secondary" to="/main/visualization/1">{this.props.t('visualisation_button')}</Button> */}
                        <DropdownButton className="ml-1" variant="secondary" title={this.props.t('visualisation_button')}>
                            <Dropdown.Item as={NavLink} variant="secondary" to="/main/visualization/1">Action</Dropdown.Item>
                            {/* <Dropdown.Item as={NavLink} variant="secondary" to="/main/visualization/2">Action</Dropdown.Item>
                            <Dropdown.Item as={NavLink} variant="secondary" to="/main/visualization/3">Action</Dropdown.Item> */}
                        </DropdownButton>
                    </Row>
                </Col>
                <Col sm={11}>
                    <HomePageContent {...this.state} />
                </Col>
            </Container>
        )
    }

    componentDidUpdate() {
        if (this.state.currentLanguage !== i18n.language) {
            this.getDropdownValues()
            this.setState({
                currentLanguage: i18n.language
            })
        }

    }
}


export default withTranslation('mainpage')(MainPage)
