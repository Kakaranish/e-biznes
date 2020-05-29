import React from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getFormDataJsonFromEvent, doRequest } from '../../../common/Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const AddOpinionForm = ({ productId, auth }) => {

    const history = useHistory();

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        formData.productId = productId;

        try {
            const action = async () => axios.post('/api/opinions', formData, {
                validateStatus: false,
                headers: { 'X-Auth-Token': auth.token }
            });
            await doRequest(action);
            history.go();
        } catch (error) {
            alert(`${error} error occured`);
        }
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

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(AddOpinionForm);