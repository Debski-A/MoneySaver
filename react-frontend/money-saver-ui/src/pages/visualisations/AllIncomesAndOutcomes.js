import React, { Component } from 'react'
import { VictoryChart, VictoryLine, VictoryTheme, VictoryAxis } from 'victory'
import { withTranslation } from "react-i18next";
import DatePicker from 'react-datepicker'
import { addDays, subDays } from 'date-fns';
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import Container from 'react-bootstrap/Container'
import i18n from '../../helpers/i18n'
import { getAccessToken } from '../../helpers/authenticationUtils'
import DropdownButton from 'react-bootstrap/DropdownButton'
import Dropdown from 'react-bootstrap/Dropdown'

class AllIncomesAndOutcomes extends Component {

    state = {
        startDate: subDays(new Date(), 2),
        endDate: new Date(),
        currency: 'PLN',
        data: [{ x: subDays(new Date(), 2).toISOString().split('T')[0], y: 0.0 },
        { x: new Date().toISOString().split('T')[0], y: 0.0 }]
    }

    handleStartDate = (date) => {
        this.setState({
            startDate: date
        }, this.triggerCalculation)
    }

    handleEndDate = (date) => {
        this.setState({
            endDate: date
        }, this.triggerCalculation)
    }

    handleCurrencyChange = (currency) => {
        console.log(currency)
        this.setState({
            currency: currency
        }, this.triggerCalculation)
    }

    triggerCalculation = () => {
        this.getData()
    }

    getData = () => {

        let incomeUpdateURL = "http://localhost/api/calculations/calculate"
        let currentLanguage = i18n.language
        let calculationData = {
            "calculationType": "BOTH",
            "currency": this.state.currency,
            "startDate": this.state.startDate,
            "endDate": this.state.endDate
        }
        fetch(incomeUpdateURL, {
            method: 'POST',
            body: JSON.stringify(calculationData),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => {
                if (!!response.errorMessage) {
                    this.setState({
                        errorMessage: response.errorMessage,
                        message: '',
                        data: [{ x: this.state.startDate.toISOString().split('T')[0], y: 0.0 },
                        { x: this.state.endDate.toISOString().split('T')[0], y: 0.0 }]
                    })
                }
                else if (!!response.status && response.status !== 200) {
                    throw new Error('Status not OK')
                }
                else {

                    let rawData = response.visualisationPoints
                    let responseData = rawData.map(i => {
                        return { x: new Date(i.date).toISOString().split('T')[0], y: i.value }
                    })
                    // let firstItem = {x: addHours(subDays(new Date(responseData[0].x), 1), 1).toISOString().split('T')[0], y: 0}
                    // responseData.unshift(firstItem)
                    // console.log(firstItem)
                    let firstItem = { x: this.state.startDate.toISOString().split('T')[0], y: 0 }
                    let lastItemIndex = Object.keys(responseData).length - 1
                    let lastItem = { x: this.state.endDate.toISOString().split('T')[0], y: responseData[lastItemIndex].y }
                    responseData.unshift(firstItem)
                    responseData.push(lastItem)
                    console.log(responseData)
                    this.setState({
                        data: responseData
                    })

                }
            })
            .catch((err) => {
                console.log(err)
                this.setState({
                    errorMessage: this.props.t('update_error'),
                    message: '',
                    data: [{ x: this.state.startDate.toISOString().split('T')[0], y: 0.0 },
                    { x: this.state.endDate.toISOString().split('T')[0], y: 0.0 }]
                })
            });
    }

    render() {
        return (
            <Container fluid className="d-flex fill-height">
                <VictoryChart width={1250} height={700} theme={VictoryTheme.grayscale}>
                    <VictoryAxis tickCount={15} crossAxis={false} domain={[0, 1000]}
                        style={{
                            axis: { stroke: '#000' },
                            ticks: { stroke: 'red', size: 5 },
                            tickLabels: { fontSize: 10, padding: 0 },
                            grid: { stroke: '#B3E5FC', strokeWidth: 0.5 }
                        }} dependentAxis
                    />
                    <VictoryAxis tickCount={10}
                        style={{
                            axis: { stroke: '#000' },
                            axisLabel: { fontSize: 10 },
                            ticks: { stroke: 'red', size: 5 },
                            tickLabels: { fontSize: 10, padding: 1, angle: 55, verticalAnchor: 'end', textAnchor: 'start' }
                        }}
                    />
                    {/* MIESIĄCE SA OZNACZONE CYFRAMI 0-11 */}
                    <VictoryLine animate={{ duration: 1000 }} data={this.state.data} />
                </VictoryChart>


                <Col>
                    <Row>
                        <div style={{ textAlign: "center", width: "100%" }}>{this.props.t('from')}</div>
                        <DatePicker inline
                            selected={this.state.startDate}
                            onChange={this.handleStartDate}
                            selectsStart
                            startDate={this.state.startDate}
                            endDate={this.state.endDate}
                            maxDate={subDays(this.state.endDate, 2)}
                        />
                    </Row>
                    <Row>
                        <div style={{ textAlign: "center", width: "100%" }}>{this.props.t('to')}</div>
                        <DatePicker inline
                            selected={this.state.endDate}
                            onChange={this.handleEndDate}
                            selectsEnd
                            startDate={this.state.startDate}
                            endDate={this.state.endDate}
                            minDate={addDays(this.state.startDate, 2)}
                        />
                    </Row>
                    <Row>
                        <div style={{ textAlign: "center", width: "100%" }}>{this.props.t('chosen_currency')}: {this.state.currency}</div>
                        <DropdownButton size="sm" variant="secondary" drop="left" title={this.props.t('currency')} >
                            {Object.keys(this.props.currencies).map((key) => <Dropdown.Item key={key} onClick={() => this.handleCurrencyChange(this.props.currencies[key])}>{this.props.currencies[key]}</Dropdown.Item>)}
                        </DropdownButton>
                    </Row>
                </Col>
            </Container>
        )
    }
}

export default withTranslation('all_incomes_and_outcomes')(AllIncomesAndOutcomes)