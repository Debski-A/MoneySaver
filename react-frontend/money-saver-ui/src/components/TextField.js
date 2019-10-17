import React from 'react'

const TextField = (props) => {
    return (
        <input onChange={props.onChange} id={props.id}>
        </input>
    )
}

export default TextField;