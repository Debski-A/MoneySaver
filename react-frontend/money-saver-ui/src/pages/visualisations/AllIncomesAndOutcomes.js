import React, { Component } from 'react'
import { VictoryChart, VictoryLine, VictoryTheme, VictoryLabel, VictoryAxis } from 'victory'
import { withTranslation } from "react-i18next";
import DatePicker from 'react-datepicker'
import { addDays, subDays } from 'date-fns';
import Col from 'react-bootstrap/Col'
import Container from 'react-bootstrap/Container'
import i18n from '../../helpers/i18n'
import { authenticate, isAuthenticated, getAccessToken } from '../../helpers/authenticationUtils'

class AllIncomesAndOutcomes extends Component {

    state = {
        startDate: subDays(new Date(), 2),
        endDate: new Date(),
        data: []
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

    triggerCalculation = () => {
        this.getData()
    }

    getData = () => {
        if (!isAuthenticated) authenticate()

        let incomeUpdateURL = "http://localhost/api/calculations/calculate"
        let currentLanguage = i18n.language
        let calculationData = {
            "calculationType": "BOTH",
            "currency": "PLN",
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
                        message: ''
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
                    let firstItem = {x: subDays(new Date(responseData[0].x), 1).toISOString().split('T')[0], y: 0}
                    responseData.unshift(firstItem)
                    console.log(firstItem)
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
                    message: ''
                })
            });
    }

    render() {
        return (
            <Container fluid className="d-flex fill-height">
                <VictoryChart  style={{  parent: { maxWidth: "80%", maxHeight: "100%" } }} theme={VictoryTheme.grayscale}>
                    <VictoryAxis tickCount={15} crossAxis={false}
                        style={{
                            axis: { stroke: '#000' },
                            axisLabel: { fontSize: 10 },
                            ticks: { stroke: 'red', size: 5 },
                            tickLabels: { fontSize: 6, padding: 1 },
                            grid: { stroke: '#B3E5FC', strokeWidth: 0.5 }
                        }} dependentAxis
                    />
                    <VictoryAxis tickCount={10}
                        style={{
                            axis: { stroke: '#000' },
                            axisLabel: { fontSize: 10 },
                            ticks: { stroke: 'red', size: 5 },
                            tickLabels: { fontSize: 6, padding: 1, angle: 65, verticalAnchor: 'end', textAnchor: 'start' }
                        }}
                    />
                    {/* MIESIĄCE SA OZNACZONE CYFRAMI 0-11 */}
                    <VictoryLine animate={{duration: 1000}} data={this.state.data} />
                </VictoryChart>
                <Col sm={2} className="flex-column mt-3">
                    <DatePicker
                        selected={this.state.startDate}
                        onChange={this.handleStartDate}
                        selectsStart
                        startDate={this.state.startDate}
                        endDate={this.state.endDate}
                        maxDate={subDays(this.state.endDate, 2)}
                    />
                    <DatePicker
                        selected={this.state.endDate}
                        onChange={this.handleEndDate}
                        selectsEnd
                        startDate={this.state.startDate}
                        endDate={this.state.endDate}
                        minDate={addDays(this.state.startDate, 2)}
                    />
                    {/* <DatePicker className="p-2" locale={this.props.i18n.language} inline id="date" /> */}
                </Col>
            </Container>
        )
    }
}

export default withTranslation('visualisations')(AllIncomesAndOutcomes)