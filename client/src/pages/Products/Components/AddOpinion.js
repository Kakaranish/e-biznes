import React from 'react';
import { createAuthAwareComponent, getFormDataJsonFromEvent } from '../../../Utils';
import axios from 'axios';

const AddOpinion = (props) => {

    const productId = props.productId;

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        formData.productId = productId;

        const result = await axios.post('/api/opinions', formData, {
            validateStatus: false,
            headers: { 'X-Auth-Token': props.auth.token }
        })

        if (result.status !== 200) {
            alert('Some error occured');
            console.log(result.data);
            return;
        }

        props.callback();
    };

    return <>
        <h3>Add opinion</h3>
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <textarea name="content" className="form-control w-50" rows="3" required>
                </textarea>
            </div>
            <button className="btn btn-primary px-5">
                Add
            </button>
        </form>
    </>
};

export default createAuthAwareComponent(AddOpinion);