import React from 'react'
import Button from 'react-bootstrap/Button'

const SubmitButton = (props) => {
    return (
        <Button variant="secondary" onClick={props.onClick}>{props.value}</Button>
    )
}

export default SubmitButton;