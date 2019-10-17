import React from 'react'

const SubmitButton = (props) => {
    return (
        <button onClick={props.onClick}>{props.value}</button>
    )
}

export default SubmitButton;